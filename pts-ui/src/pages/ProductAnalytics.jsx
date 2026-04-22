import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import PriceChart from '../components/PriceChart';
import { ArrowLeftIcon } from '@heroicons/react/24/outline'; // Если используешь Heroicons

const ProductAnalytics = () => {
  const { productId } = useParams(); // Получаем ID из пути /analytics/:productId
  const [history, setHistory] = useState([]);
  const [product, setProduct] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Здесь должен быть вызов к твоему History Service или API Gateway
    const fetchAnalytics = async () => {
      try {
        setIsLoading(true);
        // Имитация запроса к API
        // const response = await fetch(`/api/history/${productId}`);
        // const data = await response.json();
        
        // Моковые данные для примера
        const mockHistory = [
          { date: '2023-10-01', price: 45000 },
          { date: '2023-10-05', price: 44200 },
          { date: '2023-10-10', price: 46000 },
          { date: '2023-10-15', price: 42000 },
          { date: '2023-10-20', price: 41500 },
        ];
        
        setHistory(mockHistory);
        setProduct({ name: 'Смартфон XYZ', currentPrice: 41500 });
      } catch (error) {
        console.error("Ошибка загрузки аналитики:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAnalytics();
  }, [productId]);

  if (isLoading) return <div className="p-10 text-center">Загрузка графиков...</div>;

  return (
    <div className="max-w-4xl mx-auto p-6">
      {/* Кнопка назад */}
      <Link to="/" className="inline-flex items-center text-sm text-blue-600 hover:underline mb-6">
        <ArrowLeftIcon className="w-4 h-4 mr-1" />
        Вернуться к подпискам
      </Link>

      <header className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">{product?.name}</h1>
        <p className="text-gray-500">Детальный отчет об изменении цены за последние 30 дней</p>
      </header>

      {/* Основной контейнер с графиком */}
      <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-lg font-semibold text-gray-800">Динамика стоимости</h2>
          <div className="text-right">
            <span className="text-sm text-gray-400">Текущая цена:</span>
            <p className="text-xl font-bold text-green-600">{product?.currentPrice} ₽</p>
          </div>
        </div>
        
        {/* Вставляем наш компонент */}
        <PriceChart data={history} />
      </div>

      {/* Дополнительная карточка со статистикой */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
        <div className="bg-blue-50 p-4 rounded-xl">
          <p className="text-xs text-blue-600 font-medium uppercase">Минимум</p>
          <p className="text-lg font-bold">41 500 ₽</p>
        </div>
        <div className="bg-red-50 p-4 rounded-xl">
          <p className="text-xs text-red-600 font-medium uppercase">Максимум</p>
          <p className="text-lg font-bold">46 000 ₽</p>
        </div>
        <div className="bg-gray-50 p-4 rounded-xl">
          <p className="text-xs text-gray-600 font-medium uppercase">Разница</p>
          <p className="text-lg font-bold text-green-600">-9.7%</p>
        </div>
      </div>
    </div>
  );
};

export default ProductAnalytics;