/*
 * Copyright 2017 EpochArch.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.epocharch.accio.asm;

import com.epocharch.accio.dto.Constant;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Created by chenyuyao on 2017/9/15.
 */
public class FieldClassAdapter extends ClassAdapter {
    public FieldClassAdapter(ClassVisitor cv){
        super(cv);
    }
    @Override
    public void visitEnd(){
        cv.visitField(Opcodes.PUTFIELD, Constant.trace_id, Type.getDescriptor(String.class),"",null);
        cv.visitField(Opcodes.PUTFIELD, Constant.request_hop, Type.getDescriptor(String.class),"",null);
        cv.visitField(Opcodes.PUTFIELD, Constant.req_id, Type.getDescriptor(String.class),"",null);
    }
}
