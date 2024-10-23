package com.karova.messaging_service.domain.msgrelation;

public enum RelationRole {
    SENDER("sender"),
    RECEIVER("receiver");

    public String value;
    RelationRole(String value) {
        this.value = value;
    }
}
