package dev.staticsaches.knes

import dev.staticsaches.knes.emulator.NES
import dev.staticsaches.knes.utils.UInt16
import dev.staticsanches.kge.engine.KotlinGameEngine
import dev.staticsanches.kge.engine.state.input.KeyboardKey
import dev.staticsanches.kge.engine.state.input.KeyboardKeyAction
import dev.staticsanches.kge.engine.state.input.KeyboardModifiers
import web.html.HTMLElement
import web.uievents.KeyboardEvent

class KNES :
    KotlinGameEngine(),
    KNESBase {
    override val nes: NES
        get() = internalNES ?: throw IllegalStateException("Engine is not running")
    private var internalNES: NES? = null
    override val mapAsm: MutableMap<UInt16, String> = mutableMapOf()
    override val spaceKey: KeyboardKey
        get() = KeyboardKey.Space
    override val rKey: KeyboardKey
        get() = KeyboardKey.KeyR
    override val iKey: KeyboardKey
        get() = KeyboardKey.KeyI
    override val nKey: KeyboardKey
        get() = KeyboardKey.KeyN
    override val xKey: KeyboardKey
        get() = KeyboardKey.KeyX

    fun run(canvasHolder: HTMLElement) =
        run(canvasHolder) {
            screenWidth = 680
            screenHeight = 480
            pixelWidth = 2
            pixelHeight = 2
        }

    override suspend fun onUserCreate() {
        internalNES = NES()
        internalOnUserCreate()
    }

    override suspend fun onKeyboardEvent(
        key: KeyboardKey,
        newAction: KeyboardKeyAction,
        newModifiers: KeyboardModifiers,
        event: KeyboardEvent,
    ) = internalOnKeyEvent(key, newAction)

    override suspend fun onUserUpdate(): Boolean = internalOnUserUpdate()

    override suspend fun onUserDestroy(): Boolean {
        val nes = internalNES
        internalNES = null
        nes?.close()
        return true
    }
}
