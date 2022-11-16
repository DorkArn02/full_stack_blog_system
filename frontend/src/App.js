import React from 'react'
import { Navbar } from './components/Navbar'
import { Outlet } from 'react-router-dom'
import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:8080';

function App() {
  return (
    <>
      <Navbar />
      <Outlet />
    </>
  )
}

export default App
