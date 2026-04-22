import axios from 'axios';

// Создаем экземпляр axios с базовой конфигурацией
const apiClient = axios.create({
  // Замените на URL вашего API Gateway или конкретного микросервиса
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // 10 секунд
});

/**
 * Сервис для работы с подписками (Subscription Service)
 */
export const subscriptionService = {
  // Получить все активные подписки пользователя
  getSubscriptions: async () => {
    const response = await apiClient.get('/subscriptions');
    return response.data;
  },

  // Добавить новый товар для отслеживания
  createSubscription: async (productData) => {
    // Ожидаем объект: { url: "...", targetPrice: 100 }
    const response = await apiClient.post('/subscriptions', productData);
    return response.data;
  },

  // Удалить подписку
  deleteSubscription: async (id) => {
    await apiClient.delete(`/subscriptions/${id}`);
  }
};

/**
 * Сервис для работы с историей цен (History & Analytics Service)
 */
export const historyService = {
  // Получить историю изменений цены для конкретного товара
  getPriceHistory: async (productId) => {
    const response = await apiClient.get(`/history/${productId}`);
    return response.data; // Ожидаем массив точек для графика
  },

  // Получить краткую аналитику (мин/макс цена)
  getStats: async (productId) => {
    const response = await apiClient.get(`/history/${productId}/stats`);
    return response.data;
  }
};

export default apiClient;