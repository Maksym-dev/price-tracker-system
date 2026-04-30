import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import PriceChart from '../components/PriceChart';
import { ArrowLeftIcon } from '@heroicons/react/24/outline'; // Если используешь Heroicons
import { productService, historyService } from '../services/api';

const ProductAnalytics = () => {
  const { t } = useTranslation();

  const { productId } = useParams(); // Получаем ID из пути /analytics/:productId
  const [history, setHistory] = useState([]);
  const [product, setProduct] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

    const { data: productById } = useQuery({
        queryKey: ['product', productId],
        queryFn: () => productService.getProductById(productId)
    });

    const { data: historyFromDb } = useQuery({
        queryKey: ['historyFromDb', productId],
        queryFn: () => historyService.getPriceHistory(productId)
    });

  useEffect(() => {
    // Здесь должен быть вызов к твоему History Service или API Gateway
    const fetchAnalytics = async () => {
      try {
        setIsLoading(true);
        // Моковые данные для примера
        const mockHistory = [
          { date: '2023-10-01', price: 45000 },
          { date: '2023-10-05', price: 44200 },
          { date: '2023-10-10', price: 46000 },
          { date: '2023-10-15', price: 42000 },
          { date: '2023-10-20', price: 41500 },
        ];
        
        if (historyFromDb?.content.length === 0) {
           setHistory(mockHistory);
        } else {
            setHistory(historyFromDb?.content);
        }

        if (productById == null) {
           setProduct({ title: 'Смартфон XYZ', currentPrice: 41500 });
        } else {
            setProduct(productById);
        }
      } catch(error) {
        console.log(error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAnalytics();

  }, [productId, productById, historyFromDb]);

  if (isLoading) return <div className="p-10 text-center">Загрузка графиков...</div>;

  return (
    <div className="max-w-4xl mx-auto p-6">
      {/* Кнопка назад */}
      <Link to="/" className="inline-flex items-center text-sm text-blue-600 hover:underline mb-6">
        <ArrowLeftIcon className="w-4 h-4 mr-1" />
        {t('back_to_subscriptions')}
      </Link>

      <header className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">{product?.title}</h1>
        <p className="text-gray-500">{t('price_report_description')}</p>
      </header>

      {/* Основной контейнер с графиком */}
      <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-lg font-semibold text-gray-800">{t('chart_title')}</h2>
          <div className="text-right">
            <span className="text-sm text-gray-400">{t('current_price')}:</span>
            <p className="text-xl font-bold text-green-600">{product?.currentPrice} ₴</p>
          </div>
        </div>
        
        {/* Вставляем наш компонент */}
        <PriceChart data={history} />
      </div>

      {/* Дополнительная карточка со статистикой */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mt-6">
        <div className="bg-blue-50 p-4 rounded-xl">
          <p className="text-xs text-blue-600 font-medium uppercase">{t('min')}</p>
          <p className="text-lg font-bold">41 500 ₴</p>
        </div>
        <div className="bg-red-50 p-4 rounded-xl">
          <p className="text-xs text-red-600 font-medium uppercase">{t('max')}</p>
          <p className="text-lg font-bold">46 000 ₴</p>
        </div>
        <div className="bg-gray-50 p-4 rounded-xl">
          <p className="text-xs text-gray-600 font-medium uppercase">{t('difference')}</p>
          <p className="text-lg font-bold text-green-600">-9.7%</p>
        </div>
      </div>
    </div>
  );
};

export default ProductAnalytics;