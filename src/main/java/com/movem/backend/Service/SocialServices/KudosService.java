package com.movem.backend.Service.SocialServices;

public interface KudosService {

    void giveKudos(Integer sessionId);

    void removeKudos(Integer sessionId);

    long getKudosCount(Integer sessionId);

    boolean hasGivenKudos(Integer sessionId);
}