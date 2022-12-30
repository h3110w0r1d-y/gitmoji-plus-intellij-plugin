package com.github.h3110w0r1dy.gitmojiplusintellijplugin.services

import com.github.h3110w0r1dy.gitmojiplusintellijplugin.MyBundle

class MyApplicationService {

    init {
        println(MyBundle.message("applicationService"))

        System.getenv("CI")
            ?: TODO("Don't forget to remove all non-needed sample code files with their corresponding registration entries in `plugin.xml`.")
    }
}
