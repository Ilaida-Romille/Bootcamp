package com.pointwest.bootcamp.hotelservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pointwest.bootcamp.hotelservices.repository.MockRoomRepositoryImpl;
import com.pointwest.bootcamp.hotelservices.repository.RoomRepository;

@Configuration
public class RepositoryConfig {
    
    @Bean
    public RoomRepository mockRoomRepository() {
        return new MockRoomRepositoryImpl();
    }

    // @Bean
    // public RoomRepository jpaRoomRepository() {
    //     return new 
    // }
}
