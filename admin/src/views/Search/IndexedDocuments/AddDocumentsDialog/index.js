import React, {useEffect, useState} from "react";
import {
  Dialog,
  AppBar,
  Toolbar,
  IconButton,
  Typography,
  Slide,
  DialogContent,
  Step, StepLabel,
  Stepper,
  DialogActions,
  Button
} from "@material-ui/core";
import {makeStyles} from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import SelectSource from "./SelectSource";
import ItemProgressDialog from "./ItemProgressDialog";
import {useKeycloak} from "@react-keycloak/web";
import {indexService, nerService} from "../../../../services";
import ReviewTable from "./ReviewTable";

const useStyles = makeStyles(theme => ({
  appBar: {
    position: 'relative',
  },
  title: {
    marginLeft: theme.spacing(2),
    flex: 1,
    color: theme.palette.common.white
  },
  backButton: {
    marginRight: theme.spacing(1),
  },
}));

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

function AddDocumentsDialog({open, onClose}) {

  const classes = useStyles();
  const {keycloak} = useKeycloak();
  const [activeStep, setActiveStep] = useState(0);

  const [itemProgressDialogOpen, setItemProgressDialogOpen] = useState(false);
  const [currentItem, setCurrentItem] = useState(null);
  const [completed, setCompleted] = useState(0);

  const [files, setFiles] = useState([]);
  const [urls, setUrls] = useState([]);

  const [itemSuggestedAnnotations, setItemSuggestedAnnotations] = useState([]);
  const [itemSelectedAnnotations, setItemSelectedAnnotations] = useState([]);


  const [sourceType, setSourceType] = useState('files');
  const [nextButtonEnabled, setNextButtonEnabled] = useState(false);

  const steps = [
    {value: 0, label: 'Select source'},
    {value: 1, label: 'Review'}
  ];

  useEffect(() => {
    setItemSelectedAnnotations(itemSuggestedAnnotations);
  }, [itemSuggestedAnnotations]);

  useEffect(() => {
    if (sourceType === "files") {
      if (files.length === 0) {
        setNextButtonEnabled(false);
      } else {
        setNextButtonEnabled(true);
      }
    } else if (sourceType === "urls") {
      if (urls.length === 0) {
        setNextButtonEnabled(false);
      } else {
        setNextButtonEnabled(true);
      }
    }
  }, [sourceType, files, urls]);

  const handleFilesChanged = (files) => {
    setFiles(files);
  };

  const handleUrlsChanged = (urls) => {
    setUrls(urls);
  };

  const handleSourceTypeChanged = (type) => {
    setSourceType(type);
  };

  const handleUploadCancel = () => {
    setItemProgressDialogOpen(false);
  };

  const handleNext = async () => {
    await analyse();
    setActiveStep(activeStep + 1);
  };

  const handleSubmit = async () => {
    await submit();
  };

  const handleBack = () => {
    setActiveStep(activeStep - 1);
  };

  const handleAnnotationsSelected = (uri, selectedAnnotations) => {
    const updatingItem = itemSelectedAnnotations.find(i => i.uri === uri);
    const others = itemSelectedAnnotations.filter(i => i.uri !== uri);
    const updatedItem = Object.assign({}, updatingItem, {resources: selectedAnnotations});
    setItemSelectedAnnotations([...others, updatedItem]);
  };

  const analyse = async () => {
    setItemProgressDialogOpen(true);
    if (sourceType === "files") {
      const totalSize = files
        .map(f => f.size)
        .reduce((s1, s2) => s1 + s2, 0);
      let processedSize = 0.0;
      setCompleted(0);
      const results = [];
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        setCurrentItem(file.name);
        const result = await nerService.annotateFileAsync(file, keycloak.idToken);

        results.push({
          uri: file.name,
          label: file.name,
          resources: result.body.resources
        });

        processedSize += file.size;
        setCompleted((processedSize / totalSize) * 100);
      }
      setItemSuggestedAnnotations(results);
    } else {
    }
    setItemProgressDialogOpen(false);
  };

  const submit = async () => {
    setItemProgressDialogOpen(true);
    if (sourceType === "files") {
      const totalSize = files
        .map(f => f.size)
        .reduce((s1, s2) => s1 + s2, 0);
      let processedSize = 0.0;
      setCompleted(0);
      const results = [];
      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        setCurrentItem(file.name);
        const annotations = itemSelectedAnnotations.find(i => i.uri === file.name).resources.map(r => r.uri);
        const doc = {
          uri: file.name,
          label: file.name,
          annotations: annotations
        };
        await indexService.indexFileAsync(file, doc, keycloak.idToken);
        processedSize += file.size;
        setCompleted((processedSize / totalSize) * 100);
      }
      setItemSuggestedAnnotations(results);
    } else {

    }
    setItemProgressDialogOpen(false);
  };

  return (
    <div>
      <Dialog fullScreen open={open} TransitionComponent={Transition}>
        <AppBar className={classes.appBar}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={onClose} aria-label="close">
              <CloseIcon/>
            </IconButton>
            <Typography variant="h4" className={classes.title}>
              Add documents
            </Typography>
          </Toolbar>
        </AppBar>
        <DialogContent>
          <Stepper activeStep={activeStep}>
            {steps.map(step => (
              <Step key={step.value}>
                <StepLabel>{step.label}</StepLabel>
              </Step>
            ))}
          </Stepper>
          <SelectSource display={activeStep === 0 ? "" : "None"}
                        onFilesChanged={handleFilesChanged}
                        onUrlsChanged={handleUrlsChanged}
                        onSourceTypeChanged={handleSourceTypeChanged}/>

          <ReviewTable display={activeStep === 1 ? "" : "None"}
                       suggestedAnnotations={itemSuggestedAnnotations}
                       onUpdate={handleAnnotationsSelected}/>
        </DialogContent>
        <DialogActions>
          <Button
            disabled={activeStep === 0}
            onClick={handleBack}
            className={classes.backButton}>
            Back
          </Button>
          {activeStep === 0 && <Button disabled={!nextButtonEnabled}
                                       variant="contained"
                                       color="primary"
                                       onClick={handleNext}>
            Next
          </Button>}
          {activeStep === 1 && <Button disabled={!nextButtonEnabled}
                                       variant="contained"
                                       color="primary"
                                       onClick={handleSubmit}>
            Submit
          </Button>}
        </DialogActions>
      </Dialog>
      <ItemProgressDialog open={itemProgressDialogOpen}
                          currentItem={currentItem}
                          completed={completed}
                          onCancel={handleUploadCancel}/>
    </div>
  )
}

export default AddDocumentsDialog;
