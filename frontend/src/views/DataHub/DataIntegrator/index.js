import React, {useState} from 'react';
import {makeStyles} from '@material-ui/styles';
import {Container} from '@material-ui/core';
import Page from 'src/components/Page';
import Header from "./Header";
import SubmitFilesDialog from "./SubmitFilesDialog";

const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  }
}));

function DataIntegrator() {
  const classes = useStyles();

  const [submitFilesDialogOpen, setSubmitFilesDialogOpen] = useState(false);

  return (
    <Page
      className={classes.root}
      title="Data Integrator">
      <Container maxWidth={false}>
        <Header onSubmitFiles={() => setSubmitFilesDialogOpen(true)}/>
      </Container>
      <SubmitFilesDialog open={submitFilesDialogOpen}
                         onClose={() => setSubmitFilesDialogOpen(false)}
                         onCancel={() => setSubmitFilesDialogOpen(false)}
                         onSubmit={(files) => {
                           setSubmitFilesDialogOpen(false);
                         }}/>
    </Page>
  );
}

export default DataIntegrator;
