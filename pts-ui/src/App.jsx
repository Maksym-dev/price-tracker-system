import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import ProductAnalytics from './pages/ProductAnalytics';

const App = () => {
  return (
    <Layout>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/analytics/:productId" element={<ProductAnalytics />} />
        </Routes>
      </BrowserRouter>
    </Layout>
  );
};

export default App;