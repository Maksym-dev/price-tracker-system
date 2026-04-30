import React from 'react';
import { useTranslation } from 'react-i18next';
import { Bell, TrendingUp, Settings, Menu } from 'lucide-react';
import Navbar from './Navbar'; // Импортируем Navbar

const Layout = ({ children }) => {
    const { t } = useTranslation();

    return (
        <div className="min-h-screen bg-gray-50 flex flex-col">
            {/* Вертикальная навигация или Шапка */}
            <Navbar /> {/* Используем компонент здесь */}

            {/* Основной контент */}
            <main className="flex-grow">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    {children}
                </div>
            </main>

            {/* Подвал */}
            <footer className="bg-white border-t border-gray-200 py-6">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex flex-col md:flex-row justify-between items-center gap-4">
                    <p className="text-sm text-gray-500">
                        &copy; {new Date().getFullYear()} Price Tracker System. {t('rights_footer')}
                    </p>
                    <div className="flex gap-6 text-sm text-gray-400">
                        <a href="#" className="hover:text-blue-600">{t('help_footer')}</a>
                        <a href="#" className="hover:text-blue-600">API</a>
                        <a href="#" className="hover:text-blue-600">{t('privacy_footer')}</a>
                    </div>
                </div>
            </footer>
        </div>
    );
};

export default Layout;