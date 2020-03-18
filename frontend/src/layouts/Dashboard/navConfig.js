import React from 'react';
import DashboardIcon from '@material-ui/icons/DashboardOutlined';
import HomeIcon from '@material-ui/icons/HomeOutlined';
import PageviewIcon from '@material-ui/icons/PageviewOutlined';
import FilterVintageOutlinedIcon from '@material-ui/icons/FilterVintageOutlined';
import StorageIcon from '@material-ui/icons/Storage';

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
    subheader: 'Explore',
    items: [
      {
        title: 'Graph',
        href: '/graph',
        icon: FilterVintageOutlinedIcon
      }
    ]
  },
  {
    subheader: 'Data Hub',
    items: [
      {
        title: 'Data Integrator',
        href: '/data-integrator/tasks',
        icon: StorageIcon
      },
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
