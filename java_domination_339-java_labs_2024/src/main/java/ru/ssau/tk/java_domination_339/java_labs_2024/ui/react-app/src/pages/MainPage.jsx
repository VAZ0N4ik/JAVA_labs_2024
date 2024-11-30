import React, {useState} from 'react';
import {Settings, Calculator, LineChart, Search} from 'lucide-react';
import SettingsModal from './SettingsModal';
import FunctionOperations from '../components/FunctionOperations';
import FunctionDifferential from '../components/FunctionDifferential';
import FunctionExplorer from '../components/FunctionExplorer';
import {Sigma} from 'lucide-react';
import FunctionIntegral from './FunctionIntegral';
import {GitMerge} from 'lucide-react';
import CompositeFunctionCreator from './CompositeFunctionCreator';

const MenuCard = ({icon: Icon, title, onClick}) => (
    <button
        onClick={onClick}
        className="flex flex-col items-center p-6 bg-white dark:bg-gray-800
                   rounded-xl shadow-lg hover:shadow-xl
                   border border-gray-200 dark:border-gray-700
                   transition-all duration-200
                   hover:scale-105 active:scale-100
                   group"
    >
        <Icon className="w-12 h-12 mb-4 text-blue-500 dark:text-blue-400
                        group-hover:text-blue-600 dark:group-hover:text-blue-300
                        transition-colors duration-200"/>
        <span className="text-lg font-medium text-gray-900 dark:text-gray-100
                        group-hover:text-blue-600 dark:group-hover:text-blue-300
                        transition-colors duration-200">
            {title}
        </span>
    </button>
);

const MainPage = () => {
    const [showSettings, setShowSettings] = useState(false);
    const [showOperations, setShowOperations] = useState(false);
    const [showDifferential, setShowDifferential] = useState(false);
    const [showExplorer, setShowExplorer] = useState(false);
    const [showIntegral, setShowIntegral] = useState(false);
    const [showComposite, setShowComposite] = useState(false);


    return (
        <div className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-8 text-center text-gray-900 dark:text-white">
                Управление табулированными функциями
            </h1>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                <MenuCard
                    icon={Settings}
                    title="Настройки"
                    onClick={() => setShowSettings(true)}
                />
                <MenuCard
                    icon={Calculator}
                    title="Операции над функциями"
                    onClick={() => setShowOperations(true)}
                />
                <MenuCard
                    icon={LineChart}
                    title="Дифференцирование"
                    onClick={() => setShowDifferential(true)}
                />
                <MenuCard
                    icon={Search}
                    title="Изучение функции"
                    onClick={() => setShowExplorer(true)}
                />
                <MenuCard
                    icon={Sigma}
                    title="Вычисление интеграла"
                    onClick={() => setShowIntegral(true)}
                />
                <MenuCard
                    icon={GitMerge}
                    title="Создание композитной функции"
                    onClick={() => setShowComposite(true)}
                />
            </div>

            {/* Модальные окна */}
            <SettingsModal
                isOpen={showSettings}
                onClose={() => setShowSettings(false)}
            />

            <FunctionOperations
                isOpen={showOperations}
                onClose={() => setShowOperations(false)}
            />

            <FunctionDifferential
                isOpen={showDifferential}
                onClose={() => setShowDifferential(false)}
            />

            <FunctionExplorer
                isOpen={showExplorer}
                onClose={() => setShowExplorer(false)}
            />

            <FunctionIntegral
                isOpen={showIntegral}
                onClose={() => setShowIntegral(false)}
            />

            <CompositeFunctionCreator
                isOpen={showComposite}
                onClose={() => setShowComposite(false)}
            />
        </div>
    );
};

export default MainPage;