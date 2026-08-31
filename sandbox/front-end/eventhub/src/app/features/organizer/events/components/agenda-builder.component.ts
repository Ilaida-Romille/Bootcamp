import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AgendaCreateRequest {
    agendaDate: string;
    title: string;
    description?: string;
}

export interface TrackCreateRequest {
    name: string;
    description?: string;
    displayOrder: number;
}

export interface SessionCreateRequestDto {
    trackId: number;
    title: string;
    description?: string;
    startTime: string;
    endTime: string;
    locationOrRoom?: string;
    speakerIds?: number[];
}

export interface SpeakerSummary {
    id: number;
    fullName: string;
    organizationOrTitle?: string;
    photoUrl?: string;
}

export interface SessionSummary {
    id: number;
    title: string;
    startTime: string;
    endTime: string;
    locationOrRoom?: string;
}

export interface TrackSummary {
    id: number;
    name: string;
    description?: string;
    displayOrder: number;
    sessions: SessionSummary[];
}

export interface AgendaResponseDto {
    id: number;
    eventId: number;
    agendaDate: string;
    title: string;
    description?: string;
    tracks: TrackSummary[];
}

export interface SessionResponseDto {
    id: number;
    trackId: number;
    title: string;
    description?: string;
    startTime: string;
    endTime: string;
    locationOrRoom?: string;
    speakers: SpeakerSummary[];
}

@Injectable({
    providedIn: 'root'
})
export class AgendaApiService {

    private readonly baseUrl = '/api';
    private readonly http = inject(HttpClient);

    // -------------------------
    // AGENDAS
    // -------------------------

    getAgendasByEventId(
        eventId: string | number
    ): Observable<AgendaResponseDto[]> {
        return this.http.get<AgendaResponseDto[]>(
            `${this.baseUrl}/events/${eventId}/agendas`
        );
    }

    getAgenda(
        agendaId: string | number
    ): Observable<AgendaResponseDto> {
        return this.http.get<AgendaResponseDto>(
            `${this.baseUrl}/agendas/${agendaId}`
        );
    }

    createAgenda(
        eventId: string | number,
        payload: AgendaCreateRequest
    ): Observable<AgendaResponseDto> {
        return this.http.post<AgendaResponseDto>(
            `${this.baseUrl}/events/${eventId}/agendas`,
            payload
        );
    }

    // -------------------------
    // TRACKS
    // -------------------------

    createTrack(
        agendaId: string | number,
        payload: TrackCreateRequest
    ): Observable<TrackSummary> {
        return this.http.post<TrackSummary>(
            `${this.baseUrl}/agendas/${agendaId}/tracks`,
            payload
        );
    }

    // -------------------------
    // SESSIONS
    // -------------------------

    createSession(
        trackId: string | number,
        payload: SessionCreateRequestDto
    ): Observable<SessionResponseDto> {
        return this.http.post<SessionResponseDto>(
            `${this.baseUrl}/tracks/${trackId}/sessions`,
            payload
        );
    }
}