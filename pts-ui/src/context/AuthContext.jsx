import React, { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
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
    localStorage.removeItem('userToken');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);