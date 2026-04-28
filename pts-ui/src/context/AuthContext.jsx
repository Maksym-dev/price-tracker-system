import React, { createContext, useContext, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const queryClient = useQueryClient();

  // В реальном проекте здесь проверяется наличие JWT токена в localStorage
  const [user, setUser] = useState(localStorage.getItem('userToken'));

  const login = (email, password) => {
    // Здесь будет вызов вашего Backend API (Spring Boot)
    console.log("Logging in with:", email);
    
    // Имитация успешного входа: сохраняем токен
    const fakeToken = "jwt-token-example";
    localStorage.setItem('userToken', fakeToken);
    setUser(fakeToken);
  };

  const logout = () => {
    // 1. Удаляем токен из хранилища
    localStorage.removeItem('userToken');
    // 2. Сбрасываем состояние пользователя
    setUser(null);
    // 3. (Опционально) Очищаем кэш API, если используете React Query или Apollo
    queryClient.clear();
    // console.log("User logged out");
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);