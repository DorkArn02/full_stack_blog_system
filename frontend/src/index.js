import { ChakraProvider } from '@chakra-ui/react';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import App from "./App.js"
import { EditPage } from './components/EditPage.js';
import ErrorPage from './components/ErrorPage.js';
import { Home } from './components/Home.js';
import { Login } from './components/Login.js';
import { PostContent } from './components/PostContent.js';
import { ProfilePage } from './components/ProfilePage.js';
import { Register } from './components/Register.js';
import { SearchPage } from './components/SearchPage.js';
import { WritePost } from './components/WritePost.js';

const root = ReactDOM.createRoot(document.getElementById('root'));

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "/",
        element: <Home />
      },
      {
        path: "login",
        element: <Login />
      },
      {
        path: "/register",
        element: <Register />
      },
      {
        path: "/write",
        element: <WritePost />
      },
      {
        path: "/posts/:id",
        element: <PostContent />
      },
      {
        path: "/settings",
        element: <ProfilePage />
      },
      {
        path: "/edit/:id",
        element: <EditPage />
      },
      {
        path: "/search",
        element: <SearchPage />
      }
    ]
  },

]);



root.render(
  <ChakraProvider>
    <RouterProvider router={router} />
  </ChakraProvider>
);

