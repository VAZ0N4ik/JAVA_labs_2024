import React, { useState } from 'react';
import { Settings, Calculator, LineChart } from 'lucide-react';
import SettingsModal from './SettingsModal';
import FunctionOperations from './FunctionOperations';
import FunctionDifferential from './FunctionDifferential';
import FunctionIntegral from "./FunctionIntegral";

const MainPage = () => {
    const [showSettings, setShowSettings] = useState(false);
    const [showOperations, setShowOperations] = useState(false);
    const [showDifferential, setShowDifferential] = useState(false);
    const [showIntegral, setShowIntegral] = useState(false);
    return (
        <div className="container mx-auto p-8">
            <h1 className="text-3xl font-bold mb-8 text-center">Управление табулированными функциями</h1>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <button
                    onClick={() => setShowSettings(true)}
                    className="flex flex-col items-center p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow"
                >
                    <Settings className="w-12 h-12 mb-4 text-blue-500"/>
                    <span className="text-lg font-medium">Настройки</span>
                </button>

                <button
                    onClick={() => setShowOperations(true)}
                    className="flex flex-col items-center p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow"
                >
                    <Calculator className="w-12 h-12 mb-4 text-green-500"/>
                    <span className="text-lg font-medium">Операции над функциями</span>
                </button>

                <button
                    onClick={() => setShowDifferential(true)}
                    className="flex flex-col items-center p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow"
                >
                    <LineChart className="w-12 h-12 mb-4 text-purple-500"/>
                    <span className="text-lg font-medium">Дифференцирование</span>
                </button>

                <button
                    onClick={() => setShowIntegral(true)}
                    className="flex flex-col items-center p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow"
                >
                    <LineChart className="w-12 h-12 mb-4 text-purple-500"/>
                    <span className="text-lg font-medium">Интегрирование</span>
                </button>
            </div>


            {/* Модальные окна */}
            {showSettings && (
                <SettingsModal
                    isOpen={showSettings}
                    onClose={() => setShowSettings(false)}
                />
            )}

            {showOperations && (
                <FunctionOperations
                    isOpen={showOperations}
                    onClose={() => setShowOperations(false)}
                />
            )}

            {showDifferential && (
                <FunctionDifferential
                    isOpen={showDifferential}
                    onClose={() => setShowDifferential(false)}
                />
            )}

            {showIntegral && (
                <FunctionIntegral
                    isOpen={showIntegral}
                    onClose={() => setShowIntegral(false)}
                />
            )}
        </div>
    );
};

export default MainPage;