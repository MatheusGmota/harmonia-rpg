package br.com.api.harmonia_rpg.service.v2;

import br.com.api.harmonia_rpg.domain.dtos.AgenteDTO;
import br.com.api.harmonia_rpg.domain.dtos.CampanhaDTO;
import br.com.api.harmonia_rpg.domain.dtos.ConviteDTO;
import br.com.api.harmonia_rpg.domain.entities.Agente;
import br.com.api.harmonia_rpg.domain.entities.Campanha;
import br.com.api.harmonia_rpg.domain.entities.Convite;
import br.com.api.harmonia_rpg.domain.enums.StatusConvite;
import br.com.api.harmonia_rpg.domain.exceptions.BusinessException;
import br.com.api.harmonia_rpg.domain.exceptions.NotFoundException;
import br.com.api.harmonia_rpg.domain.mapper.CampanhaMapper;
import br.com.api.harmonia_rpg.repositories.interfaces.AgenteRepository;
import br.com.api.harmonia_rpg.repositories.interfaces.CampanhaRepository;
import br.com.api.harmonia_rpg.repositories.interfaces.ConviteRepository;
import br.com.api.harmonia_rpg.service.interfaces.CampanhaService;
import br.com.api.harmonia_rpg.service.v1.UsuarioService;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static br.com.api.harmonia_rpg.tools.UpdateTool.filterValidFields;
import static br.com.api.harmonia_rpg.tools.UpdateTool.flattenMap;

@Slf4j
@Service
public class CampanhaServiceImpl implements CampanhaService {

    @Value("${app.convite.base-url}")
    private String conviteBaseUrl;

    @Value("${app.convite.expiracao-dias:7}")
    private int conviteExpiracaoDias;

    @Autowired
    private CampanhaRepository campanhaRepository;

    @Autowired
    private ConviteRepository conviteRepository;

    @Autowired
    private AgenteRepository agenteRepository;

    @Autowired
    private UsuarioService usuarioService;

    // =========================================================================
    // CRUD Campanha
    // =========================================================================

    @Override
    public CampanhaDTO.ResponseDTO criar(String idUsuario, CampanhaDTO.RequestDTO dto) {
        try {
            usuarioService.obterUsuarioPorId(idUsuario); // valida existência do usuário

            Campanha campanha = CampanhaMapper.toCampanha(dto, idUsuario);
            Campanha salva = campanhaRepository.adicionar(campanha);

            log.info("Campanha id={} criada pelo usuário id={}", salva.getId(), idUsuario);
            return CampanhaMapper.toResponseDTO(salva, idUsuario);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao criar campanha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public CampanhaDTO.ResponseDTO obter(String idUsuario, String idCampanha) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarAcessoCampanha(campanha, idUsuario);
            return CampanhaMapper.toResponseDTO(campanha, idUsuario);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao obter campanha: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CampanhaDTO.ResponseDTO> obterMinhas(String idUsuario) {
        try {
            List<Campanha> comoMestre  = campanhaRepository.obterPorMestre(idUsuario);
            List<Campanha> comoJogador = campanhaRepository.obterPorJogador(idUsuario);

            return Stream.concat(comoMestre.stream(), comoJogador.stream())
                    .distinct()
                    .map(c -> CampanhaMapper.toResponseDTO(c, idUsuario))
                    .toList();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao listar campanhas do usuário id={}: {}", idUsuario, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> editar(String idUsuario, String idCampanha, Map<String, Object> updates) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarMestre(campanha, idUsuario); // somente mestre edita metadados da campanha

            Map<String, Object> camposFiltrados = filterValidFields(flattenMap(updates, ""), Campanha.class);

            // Campos imutáveis via PATCH — protege integridade da estrutura
            camposFiltrados.remove("id");
            camposFiltrados.remove("idMestre");
            camposFiltrados.remove("idJogadores");
            camposFiltrados.remove("idAgentes");
            camposFiltrados.remove("criadoEm");

            WriteResult result = campanhaRepository.atualizar(idCampanha, camposFiltrados);
            log.info("Campanha id={} editada em: {}", idCampanha, result.getUpdateTime());
            return Map.of(
                    "id", idCampanha,
                    "message", "Campanha atualizada com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao editar campanha id={}: {}", idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> deletar(String idUsuario, String idCampanha) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarMestre(campanha, idUsuario);

            WriteResult result = campanhaRepository.deletar(idCampanha);
            log.info("Campanha id={} deletada pelo mestre id={} em: {}", idCampanha, idUsuario, result.getUpdateTime());
            return Map.of(
                    "message", "Campanha deletada com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao deletar campanha id={}: {}", idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }


    // =========================================================================
    // Agentes dentro da campanha
    // =========================================================================

    @Override
    public List<CampanhaDTO.AgenteNaCampanhaDTO> obterAgentesDaCampanha(String idUsuario, String idCampanha) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarAcessoCampanha(campanha, idUsuario);

            if (campanha.getIdAgentes().isEmpty()) return List.of();

            boolean ehMestre = campanha.getIdMestre().equals(idUsuario);

            List<Agente> agentes = agenteRepository.obterPorIds(campanha.getIdAgentes());

            return agentes.stream()
                    .filter(agente -> ehMestre || agente.getIdUsuario().equals(idUsuario))
                    .map(agente -> CampanhaMapper.toAgenteNaCampanhaDTO(agente, campanha.getNome()))
                    .toList();

        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao obter agentes da campanha id={}: {}", idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public CampanhaDTO.ResponseDTO adicionarAgente(String idUsuario, String idCampanha, String idAgente) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarAcessoCampanha(campanha, idUsuario);

            Agente agente = agenteRepository.obter(idAgente);
            if (agente == null) throw new NotFoundException("Agente não encontrado");

            boolean ehMestre = campanha.getIdMestre().equals(idUsuario);
            boolean ehDono   = agente.getIdUsuario().equals(idUsuario);

            // Mestre pode adicionar qualquer ficha; jogador só as próprias
            if (!ehMestre && !ehDono) {
                throw new BusinessException("Você só pode adicionar suas próprias fichas à campanha");
            }

            if (!campanha.getIdAgentes().contains(idAgente)) {
                campanha.getIdAgentes().add(idAgente);

                campanhaRepository.atualizar(idCampanha, Map.of("idAgentes", campanha.getIdAgentes()));
                log.info("Agente id={} adicionado à campanha id={} pelo usuário id={}", idAgente, idCampanha, idUsuario);
            } else {
                log.info("Agente id={} já pertencia à campanha id={}", idAgente, idCampanha);
            }

            return CampanhaMapper.toResponseDTO(campanha, idUsuario);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao adicionar agente id={} à campanha id={}: {}", idAgente, idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> editarAgente(String idUsuario, String idCampanha,
                                            String idAgente, Map<String, Object> updates) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarAcessoCampanha(campanha, idUsuario);

            if (!campanha.getIdAgentes().contains(idAgente)) {
                throw new BusinessException("Esta ficha não pertence à campanha");
            }

            Agente agente = agenteRepository.obter(idAgente);
            if (agente == null) throw new NotFoundException("Agente não encontrado");

            boolean ehMestre = campanha.getIdMestre().equals(idUsuario);
            boolean ehDono   = agente.getIdUsuario().equals(idUsuario);

            // Mestre edita qualquer ficha da campanha; jogador só a própria
            if (!ehMestre && !ehDono) {
                throw new BusinessException("Você não tem permissão para editar esta ficha");
            }

            Map<String, Object> camposFiltrados = filterValidFields(flattenMap(updates, ""), Agente.class);
            camposFiltrados.remove("id");
            camposFiltrados.remove("idUsuario");

            WriteResult result = agenteRepository.atualizar(idAgente, camposFiltrados);
            log.info("Agente id={} editado na campanha id={} pelo usuário id={} em: {}",
                    idAgente, idCampanha, idUsuario, result.getUpdateTime());

            return Map.of(
                    "id", idAgente,
                    "message", "Ficha atualizada com sucesso",
                    "atualizadoEm", result.getUpdateTime()
            );
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao editar agente id={} na campanha id={}: {}", idAgente, idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> removerAgente(String idUsuario, String idCampanha, String idAgente) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarAcessoCampanha(campanha, idUsuario);

            if (!campanha.getIdAgentes().contains(idAgente)) {
                throw new BusinessException("Esta ficha não pertence à campanha");
            }

            Agente agente = agenteRepository.obter(idAgente);
            if (agente == null) throw new NotFoundException("Agente não encontrado");

            boolean ehMestre = campanha.getIdMestre().equals(idUsuario);
            boolean ehDono   = agente.getIdUsuario().equals(idUsuario);

            if (!ehMestre && !ehDono) {
                throw new BusinessException("Você não tem permissão para remover esta ficha");
            }

            campanha.getIdAgentes().remove(idAgente);
            campanhaRepository.atualizar(idCampanha, Map.of("idAgentes", campanha.getIdAgentes()));

            log.info("Agente id={} removido da campanha id={} pelo usuário id={}", idAgente, idCampanha, idUsuario);
            return Map.of("message", "Ficha removida da campanha com sucesso");
        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao remover agente id={} da campanha id={}: {}", idAgente, idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // =========================================================================
    // Convites por link / token
    // =========================================================================

    @Override
    public ConviteDTO.ResponseDTO gerarLinkConvite(String idUsuario, String idCampanha) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarMestre(campanha, idUsuario);

            // Reutiliza convite PENDENTE que ainda não expirou
            List<Convite> convitesExistentes = conviteRepository.obterPorCampanha(idCampanha);
            Optional<Convite> conviteAtivo = convitesExistentes.stream()
                    .filter(c -> c.getStatus() == StatusConvite.PENDENTE)
                    .filter(c -> c.getExpiraEm().toDate().after(Date.from(Instant.now())))
                    .findFirst();

            if (conviteAtivo.isPresent()) {
                log.info("Reutilizando convite ativo id={} para campanha id={}", conviteAtivo.get().getId(), idCampanha);
                return CampanhaMapper.toConviteResponseDTO(conviteAtivo.get(), conviteBaseUrl);
            }

            // Cria novo convite com token UUID único
            String token = UUID.randomUUID().toString();

            Convite novoConvite = new Convite();
            novoConvite.setIdCampanha(idCampanha);
            novoConvite.setNomeCampanha(campanha.getNome());
            novoConvite.setIdMestre(idUsuario);
            novoConvite.setToken(token);
            novoConvite.setStatus(StatusConvite.PENDENTE);
            novoConvite.setExpiraEm(Timestamp.ofTimeSecondsAndNanos(
                    Instant.now().plusSeconds((long) conviteExpiracaoDias * 24 * 3600).getEpochSecond(), 0));

            Convite salvo = conviteRepository.adicionar(novoConvite);
            log.info("Novo link de convite gerado id={} para campanha id={}", salvo.getId(), idCampanha);
            return CampanhaMapper.toConviteResponseDTO(salvo, conviteBaseUrl);

        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao gerar link de convite para campanha id={}: {}", idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public ConviteDTO.AceitarResponseDTO aceitarPorToken(String idUsuario, String token) {
        try {
            Convite convite = conviteRepository.obterPorToken(token);

            if (convite == null) {
                throw new NotFoundException("Link de convite inválido");
            }

            if (convite.getStatus() == StatusConvite.REVOGADO) {
                throw new BusinessException("Este link de convite foi revogado pelo mestre");
            }

            if (convite.getStatus() == StatusConvite.EXPIRADO) {
                throw new BusinessException("Este link de convite expirou");
            }

            // Verifica expiração no momento do uso (fonte de verdade)
            if (convite.getExpiraEm().toDate().before(Date.from(Instant.now()))) {
                conviteRepository.atualizar(convite.getId(), Map.of("status", StatusConvite.EXPIRADO.name()));
                log.info("Convite id={} expirado no momento do uso", convite.getId());
                throw new BusinessException("Este link de convite expirou");
            }

            Campanha campanha = obterOuLancar(convite.getIdCampanha());

            // Mestre não pode entrar na própria campanha como jogador
            if (campanha.getIdMestre().equals(idUsuario)) {
                throw new BusinessException("Você já é o mestre desta campanha");
            }

            // Idempotente: jogador já presente não gera erro
            if (campanha.getIdJogadores().contains(idUsuario)) {
                log.info("Usuário id={} já é jogador da campanha id={}", idUsuario, campanha.getId());
                return new ConviteDTO.AceitarResponseDTO(
                        campanha.getId(),
                        campanha.getNome(),
                        "Você já faz parte desta campanha"
                );
            }

            // Adiciona jogador à campanha
            campanha.getIdJogadores().add(idUsuario);
            campanhaRepository.atualizar(campanha.getId(), Map.of("idJogadores", campanha.getIdJogadores()));

            // Registra quem usou o link por último (link é reutilizável por padrão)
            conviteRepository.atualizar(convite.getId(), Map.of("idUsuarioConvidado", idUsuario));

            log.info("Usuário id={} entrou na campanha id={} via token", idUsuario, campanha.getId());
            return new ConviteDTO.AceitarResponseDTO(
                    campanha.getId(),
                    campanha.getNome(),
                    "Você entrou na campanha com sucesso!"
            );

        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao aceitar convite por token: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> revogarLinkConvite(String idUsuario, String idCampanha) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarMestre(campanha, idUsuario);

            List<Convite> pendentes = conviteRepository.obterPorCampanha(idCampanha)
                    .stream()
                    .filter(c -> c.getStatus() == StatusConvite.PENDENTE)
                    .toList();

            if (pendentes.isEmpty()) {
                throw new BusinessException("Não há link de convite ativo para esta campanha");
            }

            for (Convite c : pendentes) {
                conviteRepository.atualizar(c.getId(), Map.of("status", StatusConvite.REVOGADO.name()));
            }

            log.info("{} link(s) de convite revogado(s) para campanha id={} pelo mestre id={}",
                    pendentes.size(), idCampanha, idUsuario);
            return Map.of("message", "Link de convite revogado com sucesso");

        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao revogar convite da campanha id={}: {}", idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // =========================================================================
    // Gestão de jogadores
    // =========================================================================

    @Override
    public Map<String, Object> removerJogador(String idMestre, String idCampanha, String idJogador) {
        try {
            Campanha campanha = obterOuLancar(idCampanha);
            verificarMestre(campanha, idMestre);

            if (!campanha.getIdJogadores().contains(idJogador)) {
                throw new BusinessException("Este usuário não é jogador desta campanha");
            }

            campanha.getIdJogadores().remove(idJogador);
            campanhaRepository.atualizar(idCampanha, Map.of("idJogadores", campanha.getIdJogadores()));

            log.info("Jogador id={} removido da campanha id={} pelo mestre id={}", idJogador, idCampanha, idMestre);
            return Map.of("message", "Jogador removido da campanha com sucesso");

        } catch (ExecutionException | InterruptedException e) {
            log.error("Erro ao remover jogador id={} da campanha id={}: {}", idJogador, idCampanha, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // =========================================================================
    // Helpers privados
    // =========================================================================

    /**
     * Busca a campanha ou lança NotFoundException.
     * Centraliza o log de "não encontrada" para não repetir em cada método.
     */
    private Campanha obterOuLancar(String idCampanha) throws ExecutionException, InterruptedException {
        Campanha campanha = campanhaRepository.obter(idCampanha);
        if (campanha == null) {
            log.warn("Campanha id={} não encontrada", idCampanha);
            throw new NotFoundException("Campanha não encontrada");
        }
        return campanha;
    }

    /**
     * Verifica se o usuário é o Mestre da campanha.
     * Lança BusinessException caso contrário.
     */
    private void verificarMestre(Campanha campanha, String idUsuario) {
        if (!campanha.getIdMestre().equals(idUsuario)) {
            log.warn("Usuário id={} tentou ação exclusiva de mestre na campanha id={}", idUsuario, campanha.getId());
            throw new BusinessException("Apenas o mestre pode realizar esta ação");
        }
    }

    /**
     * Verifica se o usuário é Mestre ou Jogador da campanha.
     * Lança BusinessException caso o usuário não faça parte da campanha.
     */
    private void verificarAcessoCampanha(Campanha campanha, String idUsuario) {
        boolean ehMestre  = campanha.getIdMestre().equals(idUsuario);
        boolean ehJogador = campanha.getIdJogadores().contains(idUsuario);
        if (!ehMestre && !ehJogador) {
            log.warn("Usuário id={} tentou acessar campanha id={} sem ser membro", idUsuario, campanha.getId());
            throw new BusinessException("Você não faz parte desta campanha");
        }
    }
}
