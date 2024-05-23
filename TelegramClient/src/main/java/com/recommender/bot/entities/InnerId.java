package com.recommender.bot.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "sequence")
public class InnerId {
    @Id
    String id;

    @Field
    int value;

    public InnerId() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "InnerId{" +
                "id='" + id + '\'' +
                ", value=" + value +
                '}';
    }
}
