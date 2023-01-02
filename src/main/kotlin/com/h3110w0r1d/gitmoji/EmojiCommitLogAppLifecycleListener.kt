package com.h3110w0r1d.gitmoji

import com.intellij.ide.AppLifecycleListener
import javassist.ClassClassPath
import javassist.ClassPool
import javassist.CtNewMethod

class EmojiCommitLogAppLifecycleListener : AppLifecycleListener {
    override fun appFrameCreated(commandLineArgs: MutableList<String>) {
        val classPool = ClassPool.getDefault()
        classPool.appendClassPath(ClassClassPath(EmojiConverter::class.java))
        val ctClass = classPool["com.intellij.vcs.log.ui.render.GraphCommitCell"]
        if (ctClass != null) {
            ctClass.defrost()
            val converter = classPool["com.h3110w0r1d.gitmoji.EmojiConverter"].getDeclaredMethod("convert")
            ctClass.addMethod(CtNewMethod.copy(converter, ctClass, null));
            val constructor = ctClass.getConstructor("(Ljava/lang/String;Ljava/util/Collection;Ljava/util/Collection;)V")
            if (constructor != null) {
                constructor.insertBefore("\$1 = convert(\$1);")
                ctClass.toClass()
            }
        }
    }
}