package ru.netology

open class Item(val id: Int)

class Post(id: Int ): Item(id)

abstract class CrudService<T : Item> {

    protected val elements = mutableListOf<T>()

    fun add(elem: T): T {
        elements += elem
        return elements.last()
    }

    fun update(newElem: T): Boolean {
        for((index, elem) in elements.withIndex()) {
            if(newElem.id == elem.id) {
                elements[index] = newElem
                return true
            }
        }
        return false
    }

    fun delete(elem: Item) {
        elements.remove(elem)
    }

    fun print() {
        println(elements)
    }
}

class WallService : CrudService<Post>()

data class Comment(val id: Int, var text: String = "", var deleted: Boolean = false)

// класс Заметки
class Note(id: Int, val title: String = "no title", val text: String = "no text", val comments: MutableList<Comment> = mutableListOf()) : Item(id) {
    override  fun toString(): String {
        return "Note $id title($title) text($text) with comments: ${comments.filter { !it.deleted } }"
    }
}

// класс сервис для работы с Заметками - будут описаны методы для работы с заметками
class NoteService : CrudService<Note>() {

    fun createComment(noteId: Int, comment: Comment) {
        elements.find { it.id == noteId }?.comments?.add(comment)
    }

    fun deleteComment(noteId: Int, commentId: Int) {
        //elements.find { it.id == noteId }?.comments?.removeIf { it.id == commentId }
        elements.find { it.id == noteId }?.comments?.find { it.id == commentId }?.deleted = true
    }

    fun restoreComment(noteId: Int, commentId: Int) {
        elements.find { it.id == noteId }?.comments?.find { it.id == commentId }?.deleted = false
    }

    fun editComment(noteId: Int, commentId: Int, message: String) {
        elements.find { it.id == noteId }?.comments?.find { it.id == commentId }?.text = message
    }

    fun getComment(noteId: Int, commentId: Int): Comment? {
        for((indexElement, element) in elements.withIndex()) {
            if(element.id == noteId) {
                for((indexComment, comment) in element.comments.withIndex()){
                    if(comment.id == commentId) {
                        return comment
                    }
                }
            }
        }
        return null
    }
}

fun main() {

    val wall = WallService()
    wall.add(elem = Post(1))
    wall.print()

    val noteService = NoteService()
    noteService.add(Note(1))
    noteService.createComment(1, Comment(1,"Hello Generics-1"))
    noteService.createComment(1, Comment(2,"Hello Generics-2"))

    println("--- Удаление комментария ---")
    noteService.deleteComment(1,1)
    noteService.print()


    println("--- Восстановление комментария ---")
    noteService.restoreComment(1,1)
    noteService.print()

    println("--- Редактирование комментария ---")
    noteService.editComment(1,1,"Новый текст комментария")
    noteService.print()

    println("--- Получение комментария заметки по id ---")
    val comment = noteService.getComment(1,1)
    println(comment)


}