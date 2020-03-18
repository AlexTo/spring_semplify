import React, {lazy} from 'react';
import {Redirect} from 'react-router-dom';
import ErrorLayout from './layouts/Error';
import DashboardLayout from './layouts/Dashboard';
import SearchLayout from './layouts/Search';
import Overview from './views/Overview';
import Index from './views/Search/Index';
import DataIntegrator from "./views/DataHub/DataIntegrator";

export default [
  {
    path: '/',
    exact: true,
    component: () => <Redirect to="/overview"/>
  },
  {
    path: '/search',
    component: SearchLayout,
    routes: [
      {
        path: '',
        exact: true,
        component: lazy(() => import('src/views/Search/Main'))
      }
    ]
  },
  {
    path: '/errors',
    component: ErrorLayout,
    routes: [
      {
        path: '/errors/error-401',
        exact: true,
        component: lazy(() => import('src/views/Error401'))
      },
      {
        path: '/errors/error-404',
        exact: true,
        component: lazy(() => import('src/views/Error404'))
      },
      {
        path: '/errors/error-500',
        exact: true,
        component: lazy(() => import('src/views/Error500'))
      },
      {
        component: () => <Redirect to="/errors/error-404"/>
      }
    ]
  },
  {
    route: '*',
    component: DashboardLayout,
    routes: [
      {
        path: '/overview',
        exact: true,
        component: Overview
      },
      {
        path: '/data-integrator/:tab',
        exact: true,
        component: DataIntegrator
      },
      {
        path: '/indexed-documents',
        exact: true,
        component: Index
      },
      {
        component: () => <Redirect to="/errors/error-404"/>
      }
    ]
  }
];
