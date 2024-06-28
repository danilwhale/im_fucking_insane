package game.entity

import java.util.Stack

class EntityWorld {
    companion object {
        const val DEATH_HEIGHT = -100.0f
    }
    
    val entities = mutableListOf<Entity>()
    val toRemove = Stack<Entity>()
    val toAdd = Stack<Entity>()
    
    fun add(entity: Entity) {
        toAdd.push(entity)
    }
    
    fun remove(entity: Entity) {
        toRemove.push(entity)
    }
    
    inline fun <reified T> get(): T? where T : Entity {
        return entities.firstOrNull { it is T } as T?
    }

    inline fun <reified T> getAll(): List<T> where T : Entity {
        return entities.filterIsInstance<T>()
    }

    fun update(delta: Float) {
        while (!toAdd.isEmpty()) {
            entities.add(toAdd.pop())
        }
        
        while (!toRemove.isEmpty()) {
            entities.remove(toRemove.pop())
        }
         
        entities.forEach {
            if (it.box.min.y < DEATH_HEIGHT) {
                toRemove.add(it)
                return
            }
            
            it.update(delta) 
        }
    }
    
    fun render(delta: Float) {
        entities.forEach { it.render(delta) }
    }
}