package dev.jason.project.spring.vc_server.core.model;

public enum Result {
    BlockedByUser, 
    DeviceNotFound, 
    DeviceAlreadyExists, 
    Error,
    MessageTextBlank, 
    NoBlockedUsers,
    SelfBlock, 
    SelfUnblock,
    UserAlreadyBlocked,
    UserAlreadyExists, 
    UserNotBlocked, 
    UserNotFound,
    NotInLogoutQueue
}