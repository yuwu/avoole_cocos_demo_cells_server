package com.avoole.cells.util;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.avoole.cells.data.Message;
import com.avoole.cells.data.MessageType;

import java.io.IOException;
import java.lang.reflect.Type;

public class  EnumSerializer implements ObjectSerializer {

    public final static EnumSerializer INSTANCE = new EnumSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object field, Type fieldType) throws IOException {
        SerializeWriter out = serializer.out;

        if(object instanceof Message){
            Message message = (Message) object;
            if(field instanceof MessageType){
                out.write(message.getType().value());
            }
        }
    }
}
