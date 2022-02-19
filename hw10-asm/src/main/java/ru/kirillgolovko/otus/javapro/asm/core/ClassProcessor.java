package ru.kirillgolovko.otus.javapro.asm.core;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;
import ru.kirillgolovko.otus.javapro.asm.annotation.Log;

/**
 * @author kirillgolovko
 */
public class ClassProcessor {
    public static byte[] processClass(byte[] classBytes) {
        var classReader = new ClassReader(classBytes);
        var classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        var classVisitor = new ClassVisitor(Opcodes.ASM5, classWriter) {
            @Override
            public MethodVisitor visitMethod(
                    int access,
                    String name,
                    String descriptor,
                    String signature,
                    String[] exceptions)
            {
                var isStatic = (access > Opcodes.ACC_STATIC);
                return new MethodVisitorWithAnnotationScanner(
                        super.visitMethod(access, name, descriptor, signature, exceptions),
                        new Method(name, descriptor),
                        Log.class,
                        isStatic);
            }
        };
        classReader.accept(classVisitor, Opcodes.ASM5);
        return classWriter.toByteArray();
    }

    private static class MethodVisitorWithAnnotationScanner extends MethodVisitor {
        private final Method method;
        private final String wantedAnnotation;
        private final boolean isStatic;
        private boolean shouldLog = false;

        private MethodVisitorWithAnnotationScanner(
                MethodVisitor methodVisitor,
                Method method,
                Class<?> annotationType,
                boolean isStatic)
        {
            super(Opcodes.ASM5, methodVisitor);
            this.method = method;
            this.wantedAnnotation = Type.getDescriptor(annotationType);
            this.isStatic = isStatic;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
            if (descriptor.equals(wantedAnnotation)) {
                shouldLog = true;
            }
            return super.visitAnnotation(descriptor, visible);
        }

        @Override
        public void visitCode() {
            mv.visitCode();
            if (shouldLog) {
                // Put method name on stack (log first param)
                mv.visitLdcInsn(method.getName());
                Type[] argumentTypes = method.getArgumentTypes();
                // Put array size on stack
                mv.visitLdcInsn(argumentTypes.length);
                // Create array with params size
                mv.visitTypeInsn(Opcodes.ANEWARRAY, Type.getInternalName(Object.class));
                for (int i = 0; i < argumentTypes.length; ++i) {
                    // Put every param to array
                    makePutToArrCode(argumentTypes[i], i);
                }
                // Invoke log with name and params array on stack
                invokeLog();
                // Stack should be empty after that
            }
        }

        private void makePutToArrCode(Type type, int index) {
            // Dup array link, we won't lose it after putting value
            // It will be the first argument for AASTORE
            mv.visitInsn(Opcodes.DUP);
            // Push index of param, the second argument for AASTORE
            mv.visitIntInsn(Opcodes.BIPUSH, index);
            // Convert primitive to link type, or just put link type to stack
            switch (type.getSort()) {
                case Type.BOOLEAN -> {
                    mv.visitVarInsn(Opcodes.ILOAD, isStatic ? index : index + 1);
                    invokeValueOf(Boolean.class, boolean.class);
                }
                case Type.CHAR -> {
                    mv.visitVarInsn(Opcodes.ILOAD, isStatic ? index : index + 1);
                    invokeValueOf(Character.class, char.class);
                }
                case Type.BYTE -> {
                    mv.visitVarInsn(Opcodes.ILOAD, isStatic ? index : index + 1);
                    invokeValueOf(Byte.class, byte.class);
                }
                case Type.SHORT -> {
                    mv.visitVarInsn(Opcodes.ILOAD, isStatic ? index : index + 1);
                    invokeValueOf(Short.class, short.class);
                }
                case Type.INT -> {
                    mv.visitVarInsn(Opcodes.ILOAD, isStatic ? index : index + 1);
                    invokeValueOf(Integer.class, int.class);
                }
                case Type.FLOAT -> {
                    mv.visitVarInsn(Opcodes.FLOAD, isStatic ? index : index + 1);
                    invokeValueOf(Integer.class, int.class);
                }
                case Type.LONG -> {
                    mv.visitVarInsn(Opcodes.LLOAD, isStatic ? index : index + 1);
                    invokeValueOf(Long.class, long.class);
                }
                case Type.DOUBLE -> {
                    mv.visitVarInsn(Opcodes.DLOAD, isStatic ? index : index + 1);
                    invokeValueOf(Double.class, double.class);
                }
                case Type.ARRAY, Type.OBJECT -> mv.visitVarInsn(Opcodes.ALOAD, isStatic ? index : index + 1);
                default -> throw new IllegalStateException("Strange method parameter type");
            }
            // load param to array
            mv.visitInsn(Opcodes.AASTORE);
        }

        private void invokeValueOf(Class<?> wrapper, Class<?> primitive) {
            try {
                var method = wrapper.getMethod("valueOf", primitive);
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        Type.getInternalName(wrapper),
                        "valueOf",
                        Type.getMethodDescriptor(method),
                        false);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        private void invokeLog() {
            try {
                var method = Logger.class.getMethod("log", String.class, Object[].class);
                mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        Type.getInternalName(Logger.class),
                        "log",
                        Type.getMethodDescriptor(method),
                        false);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
