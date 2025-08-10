function parseJwtPayload(token) {
    const [, payloadB64] = token.split(".");
    const payloadJson = Buffer.from(payloadB64, "base64").toString("utf8");
    return JSON.parse(payloadJson);
}

module.exports = {
    parseJwtPayload
};