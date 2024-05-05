const { default: axiosClient } = require("./AxiosClient");

const testAPI = async () => {
    const response = await axiosClient.get('/v1/authentication/test-aws-deploy2');
    return response;
};

module.exports = {
    testAPI,
};