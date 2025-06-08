import org.jetbrains.kotlin.gradle.targets.js.yarn.yarn

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
        yarn.ignoreScripts = false
    }

    jvm()
    jvmToolchain(11)

    sourceSets {
        commonMain.dependencies {
            api(libs.kge.core)
        }

        jsMain.dependencies {
            api(kotlinWrappers.browser)
            api(libs.kotlinx.coroutines.core)
        }

        jvmMain.dependencies {
            implementation(libs.logback.classic)

            val name = System.getProperty("os.name")!!
            val arch = System.getProperty("os.arch")!!
            val kgeNatives =
                when {
                    "FreeBSD" == name -> libs.kge.freebsd

                    arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
                        if (arrayOf("arm", "aarch64").any { arch.startsWith(it) }) {
                            if (arch.contains("64") || arch.startsWith("armv8")) {
                                libs.kge.linux.arm64
                            } else {
                                libs.kge.linux.arm32
                            }
                        } else if (arch.startsWith("ppc")) {
                            libs.kge.linux.ppc64le
                        } else if (arch.startsWith("riscv")) {
                            libs.kge.linux.riscv64
                        } else {
                            libs.kge.linux
                        }

                    arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
                        if (arch.startsWith("aarch64")) {
                            libs.kge.macos.arm64
                        } else {
                            libs.kge.macos
                        }

                    arrayOf("Windows").any { name.startsWith(it) } ->
                        if (arch.contains("64")) {
                            if (arch.startsWith("aarch64")) {
                                libs.kge.windows.arm64
                            } else {
                                libs.kge.windows
                            }
                        } else {
                            libs.kge.windows.x86
                        }

                    else -> throw Error("Unrecognized or unsupported platform. Please set \"kgeNatives\" manually")
                }
            api(kgeNatives)
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                    optIn.add("kotlin.ExperimentalStdlibApi")
                }
            }
        }
    }
}
