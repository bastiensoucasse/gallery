FROM node:lts-alpine

WORKDIR /home/app/frontend/


COPY package*.json ./

RUN npm install

COPY . /home/app/frontend/

RUN npm run build


EXPOSE 8089

CMD [ "npm", "run", "serve" ]