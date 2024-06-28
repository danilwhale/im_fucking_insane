package engine.base

class InputManager(private val window: Window) {
    private val previousKeys = mutableMapOf<Int, Boolean>()
    private val keys = mutableMapOf<Int, Boolean>()
    
    fun pollEvents() {
        
    }
}