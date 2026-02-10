package com.javanauta.agendadortarefas.controller;

import com.javanauta.agendadortarefas.business.TarefasService;
import com.javanauta.agendadortarefas.business.dto.TarefasDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarefas")
@RequiredArgsConstructor

public class TarefasController {

    private final TarefasService tarefasService;

    @PostMapping
    public ResponseEntity<TarefasDTO> salvarTarefas(@RequestBody TarefasDTO dto,
                                                    @RequestHeader("Authorization") String token){
        return ResponseEntity.ok(tarefasService.salvarTarefa(token, dto));
    }
}
