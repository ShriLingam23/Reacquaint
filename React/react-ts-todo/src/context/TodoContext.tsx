import React, { createContext } from 'react'
import { nanoid } from 'nanoid'
import { useLocalStorage } from 'usehooks-ts'

export interface Todo {
  id: string;
  text: string;
  status: 'undone' | 'completed';
}

interface TodoContextProps {
  todos: Todo[];
  addTodo: (text: string) => void;
  deleteTodo: (id: string) => void;
  editTodo: (id: string, text: string) => void;
  updateTodoStatus: (id: string) => void;
}

export const TodoContext = createContext<TodoContextProps | undefined>(undefined)

export const TodoProvider = (props: { children: React.ReactNode }) => {

  const [todos, setTodos] = useLocalStorage<Todo[]>('todo',[])

  // ::: Add Implementation :::
  const addTodo = (text: string) => {
    const newTodo : Todo = {
      id: nanoid(),
      text,
      status: 'undone'
    }
    setTodos([...todos, newTodo])
  }

  // ::: Delete Implementation :::
  const deleteTodo = (id: string) => {
    setTodos(prevState => prevState.filter(todo => todo.id !== id))
  }

  // ::: Edit Implementation :::
  const editTodo = (id: string, text: string) => {
    setTodos(prevState => prevState.map(todo => todo.id === id ? {...todo, text} : todo))
  }

  // ::: Update Status Implementation :::
  const updateTodoStatus = (id: string) => {
    setTodos(prevState =>
      prevState.map(todo => todo.id === id ? {...todo, status: todo.status === 'undone' ? 'completed' : 'undone'} : todo))
  }

  const value: TodoContextProps = {
    todos,
    addTodo,
    deleteTodo,
    editTodo,
    updateTodoStatus
  }

  return (
    <TodoContext.Provider value={value}>
      {props.children}
    </TodoContext.Provider>
  )
}
