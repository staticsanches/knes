package dev.staticsaches.knes

import dev.staticsaches.knes.emulator.NES
import dev.staticsaches.knes.utils.UInt16
import dev.staticsanches.kge.engine.KotlinGameEngine
import dev.staticsanches.kge.engine.state.input.KeyboardKey
import dev.staticsanches.kge.engine.state.input.KeyboardKeyAction
import dev.staticsanches.kge.engine.state.input.KeyboardModifiers

class KNES :
    KotlinGameEngine("NES emulator"),
    KNESBase {
    override val nes: NES
        get() = internalNES ?: throw IllegalStateException("Engine is not running")
    private var internalNES: NES? = null
    override val mapAsm: MutableMap<UInt16, String> = mutableMapOf()
    override val spaceKey: KeyboardKey
        get() = KeyboardKey.KEY_SPACE
    override val rKey: KeyboardKey
        get() = KeyboardKey.KEY_R
    override val iKey: KeyboardKey
        get() = KeyboardKey.KEY_I
    override val nKey: KeyboardKey
        get() = KeyboardKey.KEY_N
    override val xKey: KeyboardKey
        get() = KeyboardKey.KEY_X

    fun run() =
        run {
            resizable = true
            keepAspectRatio = true
            screenWidth = 680
            screenHeight = 480
            pixelWidth = 2
            pixelHeight = 2
        }

    override fun onUserCreate() {
        internalNES = NES()
        internalOnUserCreate()
    }

    override fun onKeyEvent(
        key: KeyboardKey,
        newAction: KeyboardKeyAction,
        scancode: Int,
        newModifiers: KeyboardModifiers,
    ) = internalOnKeyEvent(key, newAction)

    override fun onUserUpdate(): Boolean = internalOnUserUpdate()

    override fun onUserDestroy(): Boolean {
        val nes = internalNES
        internalNES = null
        nes?.close()
        return true
    }
}
