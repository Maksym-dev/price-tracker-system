import React from 'react';
import { Link } from 'react-router-dom'; // Импортируем Link для навигации
import { BarChart2, ExternalLink } from 'lucide-react'; // Иконки для визуализации

export const ProductCard = ({ product }) => {
  const isTargetReached = product.currentPrice <= product.targetPrice;

  return (
    <div className="bg-white p-5 rounded-xl shadow-sm border border-gray-100 flex flex-col justify-between hover:shadow-md transition-shadow">
      <div>
        <div className="flex justify-between items-start mb-2">
          <h3 className="font-bold text-gray-900 line-clamp-2">{product.title}</h3>
          <span className={`px-2 py-1 text-xs rounded-full ${isTargetReached ? 'bg-green-100 text-green-700' : 'bg-blue-100 text-blue-700'}`}>
            {isTargetReached ? 'Цена достигнута' : 'В поиске'}
          </span>
        </div>
        <p className="text-sm text-gray-500 mb-4 truncate">{product.url}</p>
      </div>

      <div className="flex justify-between items-end border-t pt-4">
        <div>
          <p className="text-xs text-gray-400 uppercase font-semibold">Текущая цена</p>
          <p className="text-2xl font-bold text-gray-900">{product.currentPrice} ₴</p>
        </div>
        <div className="text-right">
          <p className="text-xs text-gray-400 uppercase font-semibold">Ваша цель</p>
          <p className="text-lg font-medium text-blue-600">{product.targetPrice} ₴</p>
        </div>
      </div>
      {/* Ссылка на страницу аналитики в самом низу карточки */}
      <Link
        to={`/analytics/${product.id}`}
        className="flex items-center justify-center gap-2 w-full py-2 bg-gray-50 hover:bg-blue-50 text-gray-600 hover:text-blue-600 rounded-lg text-sm font-medium transition-colors border border-gray-100 hover:border-blue-200"
      >
        <BarChart2 size={16} />
        Посмотреть историю цен
      </Link>
    </div>
  );
};