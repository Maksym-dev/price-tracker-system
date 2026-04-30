import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import App from './App';
import './index.css'; // Подключение Tailwind CSS
import './i18n'; // Импорт конфигурации локализации

// Создаем клиент для управления кэшем и запросами
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      // Отключаем автоматический перезапрос при смене вкладки браузера,
      // чтобы не спамить ваш бэкенд (Scraper/Subscription Service)
      refetchOnWindowFocus: false,
      retry: 1,
    },
  },
});

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  </React.StrictMode>
);