import React from 'react';
import DashboardIcon from '@material-ui/icons/DashboardOutlined';
import HomeIcon from '@material-ui/icons/HomeOutlined';
import PageviewIcon from '@material-ui/icons/PageviewOutlined';

export default [
  {
    subheader: 'General',
    items: [
      {
        title: 'Overview',
        href: '/overview',
        icon: HomeIcon
      }
    ]
  },
  {
    subheader: 'Data Hub',
    items: [
      {
        title: 'Entity Hub',
        href: '/entity-hub',
        icon: DashboardIcon,
        items: [
          {
            title: 'SKOS Concepts',
            href: '/entity-hub/skos',
          },
          {
            title: 'OWL Ontologies',
            href: '/entity-hub/owl'
          },
        ]
      },
      {
        title: 'Search Engine',
        href: '/search-engine',
        icon: PageviewIcon,
        items: [
          {
            title: 'Indexed Documents',
            href: '/indexed-documents',
          }
        ]
      },
    ]
  }
];
