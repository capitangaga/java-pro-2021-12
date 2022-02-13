package ru.kirillgolovko.otus.javapro.asm.demo;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import ru.kirillgolovko.otus.javapro.asm.core.ClassProcessor;

/**
 * @author kirillgolovko
 */
public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(
                    ClassLoader loader, String className,
                    Class<?> classBeingRedefined,
                    ProtectionDomain protectionDomain,
                    byte[] classfileBuffer)
            {
                return ClassProcessor.processClass(classfileBuffer);
            }
        });
    }
}
