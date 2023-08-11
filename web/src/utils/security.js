import JsEncrypt from 'jsencrypt';

const publicKey = 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl23OJ3yD1f1NYAhkrqFVb2mIpW9OPxFENEMk1n4GqySTDVghvwtdDP5JyRbwAjeb7Kx3RogHerbbQ9jffISO4/Fkeeq7ghQG+iuO1lc1lvAdIhWHZJSSxcm4YnwMVkiO3Fqqtdi9grjBkO8jLALTHZXIG4mVq/L02wCGOr407I+TtIky+ZAilDzgHzpsqH8rwQk1Yn2qzsyIA4I0FDrsvH7YXebsAxsN8t6brCHhnCeu7NKKH6KL9r9+8rdg1sD+JDBrc6cEw/nsa/8/8RM3HafwH9r5iLe3wcTtqIx7vGMruFA4xU6XdSqBOurX8/kBbnN7oC9zhO+4tPph4oqyJwIDAQAB';
const jsEncrypt = new JsEncrypt();
jsEncrypt.setPublicKey(publicKey);

export function encrypt(rawPassword) {
  if (publicKey) {
    return jsEncrypt.encrypt(rawPassword);
  }
  return rawPassword;
}
