import request from 'superagent';

const serviceUri = '/file-server';

export const fileService = {
  uploadAsync
};

async function uploadAsync(file, token) {
  const url = `${serviceUri}/files/`;
  try {
    return await request
      .post(url)
      .set('Authorization', `Bearer ${token}`)
      .attach('file', file);

  } catch (err) {
    return err
  }

}
