package com.mirkocaserta.example;

import java.time.Instant;

final class TypedTimeValue implements Comparable<TypedTimeValue> {

    enum Type {T, C}

    private final Type type;

    private final TimeValue timeValue;

    TypedTimeValue(Type type, TimeValue timeValue) {
        this.type = type;
        this.timeValue = timeValue;
    }

    public Type type() {
        return type;
    }

    public TimeValue timeValue() {
        return timeValue;
    }

    public Instant timestamp() {
        return timeValue.timestamp();
    }

    @Override
    public int compareTo(TypedTimeValue o) {
        return timestamp().compareTo(o.timestamp());
    }

}
