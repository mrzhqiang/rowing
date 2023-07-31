import JsEncrypt from 'jsencrypt';

const publicKey = '';
const jsEncrypt = new JsEncrypt();
jsEncrypt.setPublicKey(publicKey);

export function encrypt(rawPassword) {
  if (publicKey) {
    return jsEncrypt.encrypt(rawPassword);
  }
  return rawPassword;
}
