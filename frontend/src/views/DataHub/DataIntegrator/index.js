import React from 'react';
import {makeStyles} from '@material-ui/styles';
import {Container} from '@material-ui/core';
import Page from 'src/components/Page';

const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  }
}));

function DataIntegrator() {
  const classes = useStyles();

  return (
    <Page
      className={classes.root}
      title="Data Integrator">
      <Container maxWidth="lg">
        Integrator
      </Container>
    </Page>
  );
}

export default DataIntegrator;
