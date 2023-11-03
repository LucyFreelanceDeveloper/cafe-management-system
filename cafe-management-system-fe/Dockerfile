FROM node:14.15.4-alpine

WORKDIR /app

# Copying necessary files.
COPY . .

# Install dependencies
RUN npm install -g @angular/cli@13.2.2
RUN npm install

# Build project
RUN npm run build --prod

# Command we want to run when our image is executed inside a container
CMD ng serve --host 0.0.0.0 --port 4200