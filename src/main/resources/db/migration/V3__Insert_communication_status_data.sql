INSERT INTO communication_statuses (guid, code, description, is_final_state) VALUES
                                     (UUID(), 'PENDING', 'Agendamento pendente', FALSE),
                                     (UUID(), 'PROCESSING', 'Em processamento', FALSE),
                                     (UUID(), 'SENT', 'Enviado com sucesso', TRUE),
                                     (UUID(), 'FAILED', 'Falha no envio', TRUE),
                                     (UUID(), 'CANCELED', 'Cancelado pelo usuário', TRUE);