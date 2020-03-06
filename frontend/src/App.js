import React, {useState} from 'react';
import {Router} from 'react-router-dom';
import {renderRoutes} from 'react-router-config';
import {createBrowserHistory} from 'history';
import MomentUtils from '@date-io/moment';
import {Provider as StoreProvider} from 'react-redux';
import {ThemeProvider} from '@material-ui/styles';
import {MuiPickersUtilsProvider} from '@material-ui/pickers';
import 'react-perfect-scrollbar/dist/css/styles.css';
import {theme} from './theme';
import {configureStore} from './store';
import routes from './routes';
import GoogleAnalytics from './components/GoogleAnalytics';
import CookiesNotification from './components/CookiesNotification';
import ScrollReset from './components/ScrollReset';
import StylesProvider from './components/StylesProvider';
import './mixins/chartjs';
import './mixins/moment';
import './mixins/validate';
import './mixins/prismjs';
import './assets/scss/main.scss';
import * as Keycloak from "keycloak-js";
import {KeycloakProvider} from "@react-keycloak/web";
import {ApolloClient} from 'apollo-client';
import {InMemoryCache} from 'apollo-cache-inmemory';
import {setContext} from 'apollo-link-context';
import {ApolloLink} from 'apollo-link';
import {createHttpLink} from 'apollo-link-http';
import {ApolloProvider} from '@apollo/react-hooks';

const keycloakHost = process.env.REACT_APP_KEYCLOAK_HOST;
const keycloakPort = process.env.REACT_APP_KEYCLOAK_PORT;
const realm = process.env.REACT_APP_REALM;
const client_id = process.env.REACT_APP_CLIENT_ID;

const initOptions = {
  url: `http://${keycloakHost}:${keycloakPort}/auth`,
  realm: realm,
  clientId: client_id,
  onLoad: 'login-required'
};

const keycloak = Keycloak(initOptions);
const history = createBrowserHistory();
const store = configureStore();

const httpLink = createHttpLink({
  uri: '/graphql',
});

const authLink = setContext((_, {headers}) => {
  // get the authentication token from local storage if it exists
  const token = keycloak.token;
  // return the headers to the context so httpLink can read them
  return {
    headers: {
      ...headers,
      authorization: token ? `Bearer ${token}` : "",
    }
  }
});

const link = ApolloLink.from([authLink, httpLink]);


const client = new ApolloClient({
  link: link,
  cache: new InMemoryCache()
});

function App() {
  return (
    <KeycloakProvider keycloak={keycloak} initConfig={{'onLoad': initOptions.onLoad}}>
      <StoreProvider store={store}>
        <ApolloProvider client={client}>
          <ThemeProvider theme={theme}>
            <StylesProvider direction={'ltr'}>
              <MuiPickersUtilsProvider utils={MomentUtils}>
                <Router history={history}>
                  <ScrollReset/>
                  <GoogleAnalytics/>
                  <CookiesNotification/>
                  {renderRoutes(routes)}
                </Router>
              </MuiPickersUtilsProvider>
            </StylesProvider>
          </ThemeProvider>
        </ApolloProvider>
      </StoreProvider>
    </KeycloakProvider>
  );
}

export default App;
