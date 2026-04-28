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

const historyApiClient = axios.create({
  // Замените на URL вашего API Gateway или конкретного микросервиса
  baseURL: import.meta.env.VITE_HISTORY_API_BASE_URL || 'http://localhost:8084',
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

export const productService = {
  getProducts: async () => {
    const response = await apiClient.get('/products');
    return response.data;
  },
  getProductById: async (id) => {
    try {
        const response = await apiClient.get(`/products/${id}`);
        return response.data;
    } catch (error) {
        if (error.response && error.response.status === 404) {
            return null;
        }
    }
  }
};

/**
 * Сервис для работы с историей цен (History & Analytics Service)
 */
export const historyService = {
  // Получить историю изменений цены для конкретного товара
  getPriceHistory: async (productId) => {
    const response = await historyApiClient.get(`/api/history/${productId}`);
    return response.data; // Ожидаем массив точек для графика
  },

  // Получить краткую аналитику (мин/макс цена)
  getStats: async (productId) => {
    const response = await historyApiClient.get(`/api/history/${productId}/stats`);
    return response.data;
  }
};

export default apiClient;