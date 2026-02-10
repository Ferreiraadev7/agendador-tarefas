package com.javanauta.agendadortarefas.business;

import com.javanauta.agendadortarefas.business.dto.TarefasDTO;
import com.javanauta.agendadortarefas.business.mapper.TarefaUpdateConverter;
import com.javanauta.agendadortarefas.business.mapper.TarefasConverter;
import com.javanauta.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.javanauta.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.javanauta.agendadortarefas.infrastructure.excepitions.ResourceNotFound;
import com.javanauta.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.javanauta.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasConverter tarefasConverter;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateConverter tarefaUpdateConverter;

    public TarefasDTO salvarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extractEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefasConverter.paraTarefaEntity(dto);

        return tarefasConverter.paraTarefaDTO(
                tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscarTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefasConverter.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal));
    }

    public List<TarefasDTO> buscarTarefasPorEmail(String token) {
        String email = jwtUtil.extractEmailToken(token.substring(7));
        List<TarefasEntity> ListaTarefas = tarefasRepository.findByEmailUsuario(email);

        return tarefasConverter.paraListaTarefasDTO(ListaTarefas);
    }

    public void deletaTarefaPorId(String id) {
        try {
            tarefasRepository.deleteById(id);
        } catch (ResourceNotFound e) {
            throw new ResourceNotFound("Erro ao deletar tarefa por id, id inexistente" + id,
                    e.getCause());
        }
    }

        public TarefasDTO alteraStatus (StatusNotificacaoEnum status, String id){
            try {
                TarefasEntity entity = tarefasRepository.findById(id).
                        orElseThrow(() -> new ResourceNotFound("Tarefa nao encotrada " + id));
                entity.setStatusNotificacaoEnum(status);
                return tarefasConverter.paraTarefaDTO(tarefasRepository.save(entity));
            } catch (ResourceNotFound e) {
                throw new ResourceNotFound("Erro ao alterar status da tarefa " + e.getCause());

             }
        }

        public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
           try{
               TarefasEntity entity = tarefasRepository.findById(id).
                       orElseThrow(() -> new ResourceNotFound("Tarefa nao encotrada " + id));
                       tarefaUpdateConverter.updateTarefas(dto, entity);
                       return tarefasConverter.paraTarefaDTO(tarefasRepository.save(entity));
           } catch (ResourceNotFound e) {
               throw new ResourceNotFound("Erro ao alterar status da tarefa " + e.getCause());

           }

        }
    }

