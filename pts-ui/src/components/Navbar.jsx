import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Bell, TrendingUp, Settings, Menu, X, User, LogOut } from 'lucide-react';

const Navbar = () => {
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login'); // Перенаправляем на страницу входа после выхода
  };

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Левая часть: Логотип и ссылки (Desktop) */}
          <div className="flex items-center">
            <div className="flex-shrink-0 flex items-center gap-2 cursor-pointer">
              <div className="bg-blue-600 p-1.5 rounded-lg shadow-sm">
                <TrendingUp className="text-white" size={20} />
              </div>
              <span className="text-xl font-bold text-gray-900 sm:block">
                PriceTracker
              </span>
            </div>
            
            {user ? (
              <div className="hidden md:ml-8 md:flex md:space-x-4">
                <a href="#" className="px-3 py-2 text-sm font-medium text-blue-600 border-b-2 border-blue-600">
                  Дашборд
                </a>
                <a href="#" className="px-3 py-2 text-sm font-medium text-gray-500 hover:text-gray-700 hover:border-gray-300 transition-colors">
                  История цен
                </a>
                <a href="#" className="px-3 py-2 text-sm font-medium text-gray-500 hover:text-gray-700 hover:border-gray-300 transition-colors">
                  Магазины
                </a>
              </div>
            ) : (
              <div></div>
            )}
          </div>

          {/* Правая часть: Уведомления и Профиль */}
          {user ? (
          <div className="flex items-center gap-2 sm:gap-4">
            <button className="relative p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-full transition-all">
              <Bell size={20} />
              <span className="absolute top-2 right-2 h-2 w-2 rounded-full bg-red-500 ring-2 ring-white"></span>
            </button>

              <div className="hidden sm:flex items-center gap-3 pl-4 border-l border-gray-200">
                <div className="text-right">
                  <p className="text-sm font-semibold text-gray-900 leading-none">John Doe</p>
                  <p className="text-xs text-gray-500 mt-1">Premium Plan</p>
                </div>
                <div className="h-9 w-9 rounded-full bg-gradient-to-tr from-blue-600 to-indigo-600 flex items-center justify-center text-white shadow-md">
                  <User size={18} />
                </div>
                <button
                  onClick={handleLogout}
                  className="flex items-center gap-2 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                >
                  <LogOut size={18} />
                  <span>Logout</span>
                </button>
              </div>

            {/* Кнопка мобильного меню */}
            <div className="md:hidden flex items-center">
              <button 
                onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
                className="p-2 rounded-md text-gray-400 hover:text-gray-500 hover:bg-gray-100"
              >
                {isMobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
              </button>
            </div>
          </div>
          ) : (
              <div className="flex items-center gap-2 sm:gap-4">
                <Link to="/login" className="hover:underline items-center text-xl font-bold text-gray-900 sm:block">
                  Войти
                </Link>
              </div>
          )}
        </div>
      </div>

      {/* Мобильное меню (выпадающее) */}
      {user && isMobileMenuOpen && (
        <div className="md:hidden bg-white border-b border-gray-200 animate-in slide-in-from-top duration-200">
          <div className="px-2 pt-2 pb-3 space-y-1">
            <a href="#" className="block px-3 py-2 rounded-md text-base font-medium bg-blue-50 text-blue-700">Дашборд</a>
            <a href="#" className="block px-3 py-2 rounded-md text-base font-medium text-gray-500 hover:text-gray-900 hover:bg-gray-50">История цен</a>
            <a href="#" className="block px-3 py-2 rounded-md text-base font-medium text-gray-500 hover:text-gray-900 hover:bg-gray-50">Магазины</a>
          </div>
          <div className="pt-4 pb-3 border-t border-gray-200">
            <div className="flex items-center px-4">
              <div className="h-10 w-10 rounded-full bg-blue-600 flex items-center justify-center text-white">
                <User size={20} />
              </div>
              <div className="ml-3">
                <div className="text-base font-medium text-gray-800">John Doe</div>
                <div className="text-sm font-medium text-gray-500">john@example.com</div>
              </div>
            </div>
            <div className="mt-3 px-2 space-y-1">
              <button className="w-full text-left px-3 py-2 rounded-md text-base font-medium text-gray-500 hover:text-gray-900 hover:bg-gray-50 flex items-center gap-2">
                <Settings size={18} /> Настройки
              </button>
              <button
                onClick={handleLogout}
                className="w-full text-left px-3 py-2 rounded-md text-base font-medium text-red-600 hover:bg-red-50 flex items-center gap-2">
                <LogOut size={18} /> Logout
              </button>
            </div>
          </div>
        </div>
      )}
    </nav>
  );
};

export default Navbar;