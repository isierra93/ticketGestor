package com.soluciones.ticketgestor.services;

import com.soluciones.ticketgestor.models.User;

public interface UserService {
    User getUserByEmail(String email);
}
