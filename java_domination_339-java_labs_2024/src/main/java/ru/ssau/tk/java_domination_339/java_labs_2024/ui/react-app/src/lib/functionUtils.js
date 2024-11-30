import api from '../services/api';
import {saveAs} from 'file-saver';

export const uploadFunction = async (formData, fileExtension) => {
    let endpoint = '/api/function-io/input';

    switch (fileExtension) {
        case 'json':
            endpoint = '/api/function-io/input-json';
            break;
        case 'xml':
            endpoint = '/api/function-io/input-xml';
            break;
        default:
            break;
    }

    const response = await api.post(endpoint, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });

    return response.data;
};

export const saveFunction = async (functionData, format = 'binary') => {
    const endpoint = format === 'json' ? '/api/function-io/output-json' :
        format === 'xml' ? '/api/function-io/output-xml' :
            '/api/function-io/output';

    const response = await api.get(endpoint, {
        params: {hash: functionData.hash_function},
        responseType: 'blob'
    });

    const fileExtension = format === 'json' ? 'json' :
        format === 'xml' ? 'xml' : 'txt';

    saveAs(response.data, `function_${functionData.hash_function}.${fileExtension}`);
};

export const checkFunctionCapabilities = async (functionId) => {
    if (!functionId) return {canInsert: false, canRemove: false};

    try {
        const [insertResponse, removeResponse] = await Promise.all([
            api.get('/api/tabulated-function-operations/is-insert', {
                params: {functionId}
            }),
            api.get('/api/tabulated-function-operations/is-remove', {
                params: {functionId}
            })
        ]);

        return {
            canInsert: insertResponse.data,
            canRemove: removeResponse.data
        };
    } catch (error) {
        console.error('Error checking capabilities:', error);
        return {canInsert: false, canRemove: false};
    }
};