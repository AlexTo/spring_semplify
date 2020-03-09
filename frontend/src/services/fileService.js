import request from 'superagent';

const serviceUri = '/file-server';

export const fileService = {
  upload
};

function upload(file, token) {
  const url = `${serviceUri}/files/`;
  return request
    .post(url)
    .set('Authorization', `Bearer ${token}`)
    .attach('file', file)
    .then(res => res.body);
}
